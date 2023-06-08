package com.eikona.tech.config;

import org.springframework.content.fs.config.FilesystemStoreConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

import com.eikona.tech.domain.FileSystemContent;

@Configuration
public class StoreConfig {
    @Bean
    public FilesystemStoreConfigurer configurer() {
        return new FilesystemStoreConfigurer() {

            @Override
            public void configureFilesystemStoreConverters(ConverterRegistry registry) {
            	//registry.addConverter(converter());
                registry.addConverter(new Converter<FileSystemContent, String>() {

                    @Override
                    public String convert(FileSystemContent document) {
                        return document.getContentPath();
                    }
                });
            }
        };
    }

//	@Bean
//	public FilesystemStoreConfigurer contentconfigurer() {
//		return new FilesystemStoreConfigurer() {
//
//			@Override
//			public void configureFilesystemStoreConverters(ConverterRegistry registry) {
//				registry.addConverter(converter());
//			}
//		};
//	}
//    public Converter<String,String> converter() {
//		return new FilesystemStoreConverter<String,String>() {
//
//			@Override
//			public String convert(String source) {
//				return String.format("/%s", source.toString().replaceAll("-", "/"));
//			}
//		};
//	}
   
}