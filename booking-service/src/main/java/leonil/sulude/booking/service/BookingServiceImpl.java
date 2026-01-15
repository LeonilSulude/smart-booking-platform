package leonil.sulude.booking.service;

import leonil.sulude.booking.dto.BookingRequestDTO;
import leonil.sulude.booking.dto.BookingResponseDTO;
import leonil.sulude.booking.exception.BookingConflictException;
import leonil.sulude.booking.model.Booking;
import leonil.sulude.booking.model.BookingStatus;
import leonil.sulude.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    public BookingServiceImpl(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BookingResponseDTO> getAll() {
        return repository.findAll()
                .stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public Optional<BookingResponseDTO> getById(UUID id) {
        return repository.findById(id)
                .map(this::mapToResponseDTO);
    }

    @Override
    public BookingResponseDTO create(BookingRequestDTO dto) {

        boolean hasConflict = repository.existsOverlappingBooking(dto.resourceId(), dto.startTime(), dto.endTime());

        if (hasConflict) {
            throw new BookingConflictException("Resource is already booked during this time.");
        }

        Booking booking = new Booking();
        booking.setResourceId(dto.resourceId());
        booking.setCustomerName(dto.customerName());
        booking.setCustomerEmail(dto.customerEmail());
        booking.setStartTime(dto.startTime());
        booking.setEndTime(dto.endTime());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());

        Booking saved = repository.save(booking);

        return new BookingResponseDTO(
                saved.getId(),
                saved.getResourceId(),
                saved.getCustomerName(),
                saved.getCustomerEmail(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }



    @Override
    public boolean delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getResourceId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }

}
