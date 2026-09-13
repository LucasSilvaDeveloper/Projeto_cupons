package br.com.cupons.controller;

import br.com.cupons.controller.dto.CouponRequest;
import br.com.cupons.controller.dto.CouponResponse;
import br.com.cupons.domain.Coupon;
import br.com.cupons.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
public class CouponController implements ICouponController{

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@Valid @RequestBody CouponRequest request) {
        Coupon cupom = service.create(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );
        return CouponResponse.fromDomain(cupom);
    }

    @GetMapping("/{id}")
    public CouponResponse findById(@PathVariable UUID id) {
        Coupon cupom = service.findById(id);
        return CouponResponse.fromDomain(cupom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
