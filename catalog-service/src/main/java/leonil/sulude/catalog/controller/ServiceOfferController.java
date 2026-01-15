package leonil.sulude.catalog.controller;

import jakarta.validation.Valid;
import leonil.sulude.catalog.dto.ServiceOfferRequestDTO;
import leonil.sulude.catalog.dto.ServiceOfferResponseDTO;
import leonil.sulude.catalog.model.ServiceOffer;
import leonil.sulude.catalog.service.ServiceOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/offers")
public class ServiceOfferController {

    private final ServiceOfferService service;

    public ServiceOfferController(ServiceOfferService service) {
        this.service = service;
    }

    /**
     * Returns all service offers, optionally including their associated resources.
     *
     * @param includeResources If true, resources are included in the response
     * @return List of service offers
     */
    @GetMapping
    public ResponseEntity<List<ServiceOfferResponseDTO>> getAll(
            @RequestParam(defaultValue = "false") boolean includeResources) {

        List<ServiceOfferResponseDTO> offers = includeResources
                ? service.getAllWithResources()
                : service.getAll();

        return ResponseEntity.ok(offers);
    }

    /**
     * Returns a specific service offer by ID.
     *
     * @param id Offer ID
     * @return Offer if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferResponseDTO> getById(@PathVariable UUID id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new service offer.
     *
     * @param offer Request body with offer details
     * @return The created offer with location header
     */
    @PostMapping
    public ResponseEntity<ServiceOfferResponseDTO> create(@Valid @RequestBody ServiceOfferRequestDTO offer) {
        ServiceOfferResponseDTO created = service.create(offer);
        URI location = URI.create("/api/offers/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Deletes an offer by ID.
     *
     * @param id Offer ID
     * @return 204 if deleted, 404 otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
