package com.szymon.ffproject.database.entity;

import java.time.LocalDateTime;

public interface Entity {

    String getEntityID();

    LocalDateTime getCreationTime();

    LocalDateTime getModificationTime();

    void setModificationTime(LocalDateTime modificationTime);

}
