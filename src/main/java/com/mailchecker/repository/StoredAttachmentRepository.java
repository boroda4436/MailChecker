package com.mailchecker.repository;

import com.mailchecker.dto.StoredAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Bohdan on 25.08.2016.
 */
@Repository
public interface StoredAttachmentRepository extends JpaRepository<StoredAttachment, Long> {
}
