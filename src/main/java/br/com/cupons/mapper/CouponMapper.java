package br.com.cupons.mapper;

import br.com.cupons.domain.Coupon;
import br.com.cupons.entity.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponEntity toEntity(Coupon cupom) {
        return new CouponEntity(
                cupom.getId(),
                cupom.getCode(),
                cupom.getDescription(),
                cupom.getDiscountValue(),
                cupom.getExpirationDate(),
                cupom.getStatus(),
                cupom.isPublished(),
                cupom.isRedeemed()
        );
    }


    public Coupon toDomain(CouponEntity entity) {
        return Coupon.reconstitute(
                entity.getId(),
                entity.getCode(),
                entity.getDescription(),
                entity.getDiscountValue(),
                entity.getExpirationDate(),
                entity.getStatus(),
                entity.isPublished(),
                entity.isRedeemed()
        );
    }
}
