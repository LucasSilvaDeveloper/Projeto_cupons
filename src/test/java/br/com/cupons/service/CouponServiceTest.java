package br.com.cupons.service;

import br.com.cupons.domain.Coupon;
import br.com.cupons.domain.CouponStatus;
import br.com.cupons.domain.exception.CouponNotFoundException;
import br.com.cupons.entity.CouponEntity;
import br.com.cupons.mapper.CouponMapper;
import br.com.cupons.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @Mock
    private CouponRepository repository;

    @Mock
    private CouponMapper mapper;

    @InjectMocks
    private CouponService service;

    private Coupon activeCoupon;
    private CouponEntity activeEntity;
    private UUID couponId;

    @BeforeEach
    void setUp() {
        couponId = UUID.randomUUID();
        Instant futureDate = Instant.now().plus(30, ChronoUnit.DAYS);

        activeCoupon = Coupon.create("ABC123", "Cupom de teste", new BigDecimal("10.0"), futureDate, false);
        activeEntity = new CouponEntity(
                couponId, "ABC123", "Cupom de teste", new BigDecimal("10.0"),
                futureDate, CouponStatus.ACTIVE, false, false
        );
    }

    @Test
    void deveCriarCupomEDelegarPersistenciaAoRepository() {
        when(mapper.toEntity(any(Coupon.class))).thenReturn(activeEntity);
        when(repository.save(any(CouponEntity.class))).thenReturn(activeEntity);
        when(mapper.toDomain(activeEntity)).thenReturn(activeCoupon);

        Coupon result = service.create("ABC-123", "Cupom de teste", new BigDecimal("10.0"),
                Instant.now().plus(30, ChronoUnit.DAYS), false);

        assertEquals(activeCoupon.getCode(), result.getCode());
        verify(repository, times(1)).save(any(CouponEntity.class));
    }

    @Test
    void deveRetornarCupomQuandoEncontradoPorId() {
        when(repository.findById(couponId)).thenReturn(Optional.of(activeEntity));
        when(mapper.toDomain(activeEntity)).thenReturn(activeCoupon);

        Coupon result = service.findById(couponId);

        assertEquals(activeCoupon.getCode(), result.getCode());
    }

    @Test
    void deveLancarExcecaoQuandoCupomNaoEncontradoPorId() {
        when(repository.findById(couponId)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> service.findById(couponId));
    }

    @Test
    void deveExecutarSoftDeleteQuandoCupomExistir() {
        when(repository.findById(couponId)).thenReturn(Optional.of(activeEntity));
        when(mapper.toDomain(activeEntity)).thenReturn(activeCoupon);
        when(mapper.toEntity(any(Coupon.class))).thenReturn(activeEntity);

        service.delete(couponId);

        verify(repository, times(1)).save(any(CouponEntity.class));
        assertEquals(CouponStatus.DELETED, activeCoupon.getStatus());
    }

    @Test
    void deveLancarExcecaoAoDeletarCupomInexistente() {
        when(repository.findById(couponId)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> service.delete(couponId));
    }
}
