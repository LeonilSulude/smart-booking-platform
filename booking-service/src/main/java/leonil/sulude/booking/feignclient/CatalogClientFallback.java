package leonil.sulude.booking.feignclient;

import leonil.sulude.booking.dto.ServiceResourceResponseDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CatalogClientFallback implements CatalogClient {

    @Override
    public ServiceResourceResponseDTO getResourceById(UUID id) {
        // Fallback default: retorn null or neutral response
        return null;
    }
}
