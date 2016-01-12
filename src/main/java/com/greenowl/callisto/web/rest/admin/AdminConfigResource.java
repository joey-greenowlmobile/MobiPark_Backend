package com.greenowl.callisto.web.rest.admin;

import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.util.PaginationUtil;
import com.greenowl.callisto.web.rest.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@SuppressWarnings("SpringJavaAutowiringInspection")
@RestController
@RequestMapping("/api/{version}/admin/")
public class AdminConfigResource {

    @Inject
    private UserRepository userRepository;

    /**
     * GET -> /users get paginated list of users for management
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    @Transactional(readOnly = true)
    public ResponseEntity<?> getPaginatedUsers(@PathVariable("version") final String version, @RequestParam(defaultValue = "0") final Integer page) {
        Pageable pageable = PaginationUtil.generatePageRequest(page, 50);
        Page<User> dbPage = userRepository.findAll(pageable);
        List<UserDTO> dtos = new ArrayList<>();
        List<User> users = dbPage.getContent();
        users.forEach(u -> {
                    List<String> roles = u.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
                    dtos.add(UserUtil.getUserDTO(u, roles));
                }
        );

        Page<UserDTO> response = new PageImpl<>(dtos, pageable, dbPage.getTotalPages());
        return new ResponseEntity<>(response, OK);
    }

}
