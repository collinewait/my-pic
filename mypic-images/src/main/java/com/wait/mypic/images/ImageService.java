package com.wait.mypic.images;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Service
public class ImageService {

	private static String UPLOAD_ROOT = "upload-dir";
	private final ResourceLoader resourceLoader;
	private final ImageRepository imageRepository;

	private final MeterRegistry meterRegistry;

	public ImageService(ResourceLoader resourceLoader, ImageRepository imageRepository, MeterRegistry meterRegistry) {
		this.resourceLoader = resourceLoader;
		this.imageRepository = imageRepository;
		this.meterRegistry = meterRegistry;
	}

	/**
	 * This method returns Flux<Image>, a container of images that only gets created
	 * when the consumer subscribes
	 */
	public Flux<Image> findAllImages() {
		return imageRepository.findAll().log("findAll");
	}

	public Mono<Resource> findOneImage(String filename) {
		return Mono.fromSupplier(() -> resourceLoader.getResource("file:" + UPLOAD_ROOT + "/" + filename));
	}

	public Mono<Void> createImage(Flux<FilePart> files, Principal auth) {
		return files.flatMap(file -> {
			Mono<Image> saveDatabaseImage = imageRepository
					.save(new Image(UUID.randomUUID().toString(), file.filename(), auth.getName()));
			Mono<Void> copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
					.log("CreateImage-picktarget").map(destFile -> {
						try {
							destFile.createNewFile();
							return destFile;
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					}).log("CreateImage-newFile").flatMap(file::transferTo).log("CreateImage-copy");
			Mono<Void> countFile = Mono.fromRunnable(() -> {
				meterRegistry.summary("files.uploaded.bytes")
						.record(Paths.get(UPLOAD_ROOT, file.filename()).toFile().length());
			});
			/*
			 * To ensure both operations(saving to MongoDB and copying to the server)are
			 * completed, join them together with Mono.when(). Mono.when() is a kin to the
			 * A+ Promise.all() API. Each file won't be completed until the record is
			 * written to MongoDB and the file is copied to the server. The entire flow is
			 * terminated with then() so we can signal when all the files have been
			 * processed
			 */
			return Mono.when(saveDatabaseImage, copyFile, countFile);
		}).then();
	}

	@PreAuthorize("hasRole('ADMIN') or " + "@imageRepository.findByName(#filename).owner " + "== authentication.name")
	public Mono<Void> deleteImage(String filename) {
		Mono<Void> deleteDatabaseImage = imageRepository.findByName(filename).flatMap(imageRepository::delete);
		Mono<Void> deleteFile = Mono.fromRunnable(() -> {
			try {
				Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		return Mono.when(deleteDatabaseImage, deleteFile).then();
	}

	@Bean
	CommandLineRunner setUp() throws IOException {
		return (args) -> {
			FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

			Files.createDirectory(Paths.get(UPLOAD_ROOT));

			FileCopyUtils.copy("Test file", new FileWriter(UPLOAD_ROOT + "/mypic1.jpg"));
			FileCopyUtils.copy("Test file2", new FileWriter(UPLOAD_ROOT + "/awesome.jpg"));
			FileCopyUtils.copy("Test file3", new FileWriter(UPLOAD_ROOT + "/great.jpg"));
		};
	}
}
