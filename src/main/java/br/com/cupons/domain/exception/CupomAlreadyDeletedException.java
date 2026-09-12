package br.com.cupons.domain.exception;

public class CupomAlreadyDeletedException extends RuntimeException {

    public CupomAlreadyDeletedException(String couponId) {
        super("Cupom com id " + couponId + " já foi deletado.");
    }
}