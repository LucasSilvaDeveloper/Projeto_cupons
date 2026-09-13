package br.com.cupons.service;

import br.com.cupons.domain.Coupon;
import br.com.cupons.domain.exception.CouponNotFoundException;
import br.com.cupons.entity.CouponEntity;
import br.com.cupons.mapper.CouponMapper;
import br.com.cupons.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class CouponService {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    public CouponService(CouponRepository repository, CouponMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public Coupon create(String rawCode, String description, BigDecimal discountValue,
                         Instant expirationDate, Boolean published) {
        Coupon cupom = Coupon.create(rawCode, description, discountValue, expirationDate, published);
        CouponEntity savedEntity = repository.save(mapper.toEntity(cupom));
        return mapper.toDomain(savedEntity);
    }

    public Coupon findById(UUID id) {
        CouponEntity entity = repository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id.toString()));
        return mapper.toDomain(entity);
    }

    public void delete(UUID id) {
        CouponEntity entity = repository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id.toString()));

        Coupon cupom = mapper.toDomain(entity);
        cupom.delete();

        repository.save(mapper.toEntity(cupom));
    }

}
