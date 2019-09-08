package com.wait.mypic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author collinewaitire 8 Sep 2019
 */
@Service
public class ImageService {

	private static String UPLOAD_ROOT = "upload-dir";
	private final ResourceLoader resourceLoader;

	public ImageService(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	/**
	 * This method returns Flux<Image>, a container of images that only gets
	 * created when the consumer subscribes
	 */
	public Flux<Image> findAllImages() {
		try {
			/*
			 * Flux.fromIterable is used to wrap this lazy iterable - DirectoryStream,
			 * allowing us to only pull each item as demanded by the reactive streams
			 * client.
			 */
			return Flux.fromIterable(Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)))
					.map(path -> new Image(String.valueOf(path.hashCode()),
							path.getFileName().toString()));

		} catch (IOException e) {
			return Flux.empty();
		}
	}

	public Mono<Resource> findOneImage(String filename) {
		/*
		 * To delay fetching the file until the client subscribes, we wrap it with
		 * Mono.fromSupplier, and put getResource inside a lambda. If we wrote
		 * Mono.just(resourceLoader.getResource(...)), the resource fetching would
		 * have happened immediately when the method is called. By putting it inside
		 * a Java 8 supplier, that wont happen until the lambda is invoked. And
		 * because it's wrapped by a Mono, invocation won't happen until the client
		 * subscribes.
		 */
		return Mono.fromSupplier(() -> resourceLoader
				.getResource("file:" + UPLOAD_ROOT + "/" + filename));
	}

	public Mono<Void> createImage(Flux<FilePart> files) {
		return files
				.flatMap(file -> file
						.transferTo(Paths.get(UPLOAD_ROOT, file.filename()).toFile()))
				.then(); // then() lets us to wait for the entire Flux to finish,
									// yielding a Mono<Void>
	}

	public Mono<Void> deleteImage(String filename) {
		return Mono.fromRunnable(() -> {
			try {
				Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Bean
	CommandLineRunner setUp() throws IOException {
		return (args) -> {
			FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

			Files.createDirectory(Paths.get(UPLOAD_ROOT));

			FileCopyUtils.copy("Test file",
					new FileWriter(UPLOAD_ROOT + "/mypic1.jpg"));
			FileCopyUtils.copy("Test file2",
					new FileWriter(UPLOAD_ROOT + "/awesome.jpg"));
			FileCopyUtils.copy("Test file3",
					new FileWriter(UPLOAD_ROOT + "/great.jpg"));
		};
	}
}
