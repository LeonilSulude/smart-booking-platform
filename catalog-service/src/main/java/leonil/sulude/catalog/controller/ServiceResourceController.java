package leonil.sulude.catalog.controller;

import leonil.sulude.catalog.dto.ServiceResourceRequestDTO;
import leonil.sulude.catalog.dto.ServiceResourceResponseDTO;
import leonil.sulude.catalog.model.ServiceResource;
import leonil.sulude.catalog.service.ServiceResourceService;
import leonil.sulude.catalog.service.ServiceResourceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
@RestController
@RequestMapping("/api/resources")
public class ServiceResourceController {

    private final ServiceResourceService service;

    public ServiceResourceController(ServiceResourceService service) {
        this.service = service;
    }

    /**
     * Creates a new service resource.
     *
     * @param dto Request body containing resource details
     * @return The created resource and location header
     */
    @PostMapping
    public ResponseEntity<ServiceResourceResponseDTO> create(@Valid @RequestBody ServiceResourceRequestDTO dto) {
        ServiceResourceResponseDTO created = service.create(dto);
        URI location = URI.create("/api/resources/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Retrieves a service resource by ID.
     *
     * @param id Resource ID
     * @return The corresponding resource or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResourceResponseDTO> getById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a resource by its ID.
     *
     * @param id Resource ID
     * @return 204 No Content on success
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
