package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // [관리자] 상품 등록
    public ProductResponse registerProduct(ProductCreateRequest request) {
        if (productRepository.findByName(request.name()) != null) {
            throw new RuntimeException("이미 존재하는 상품입니다.");
        }

        Product newProduct = new Product(
                request.name(),
                request.price(),
                request.stock()
        );

        return ProductResponse.from(productRepository.save(newProduct));
    }

    // [관리자] 재고 추가
    public ProductResponse addStock(StockUpdateRequest request) {
        Product product = productRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        // 트랜잭션(@Transactional) 적용 전이라 아직 메모리 객체 값만 변경됨
        product.setStock(product.getStock() + request.stock());
        return ProductResponse.from(product);
    }

    // [관리자] 상품 삭제
    public List<ProductResponse> deleteProducts(List<Long> productIds) {
        for (Long id : productIds) {
            if (!productRepository.existsById(id)) {
                throw new RuntimeException("삭제할 상품을 찾을 수 없습니다.");
            }
            productRepository.deleteById(id);
        }

        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    // [소비자] 상품명으로 단건 조회
    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }

        return ProductResponse.from(product);
    }

    // [소비자] 상품 구매
    public PurchaseResponse purchaseProducts(List<PurchaseItemRequest> requests) {
        int totalPrice = 0;
        List<PurchasedProductResponse> purchasedProducts = new ArrayList<>();

        for (PurchaseItemRequest request : requests) {
            Product product = productRepository.findById(request.id())
                    .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

            if (product.getStock() < request.quantity()) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            product.setStock(product.getStock() - request.quantity());

            int itemTotalPrice = product.getPrice() * request.quantity();
            totalPrice += itemTotalPrice;

            purchasedProducts.add(new PurchasedProductResponse(
                    product.getId(),
                    product.getName(),
                    itemTotalPrice,
                    request.quantity()
            ));
        }

        return new PurchaseResponse(totalPrice, purchasedProducts);
    }
}
