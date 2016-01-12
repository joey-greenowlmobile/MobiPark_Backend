package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findOneByPushInfo(String token);
}
