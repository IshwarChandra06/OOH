package com.eikona.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.eikona.tech.domain.FileSystemContent;

@RepositoryRestResource(path="files", collectionResourceRel="files")
public interface FileRepository extends JpaRepository<FileSystemContent, Long> {

}

