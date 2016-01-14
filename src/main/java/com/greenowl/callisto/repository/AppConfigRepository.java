package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, String> {

}
