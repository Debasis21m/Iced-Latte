package com.zufar.icedlatte.product.api;

import com.zufar.icedlatte.openapi.dto.ProductInfoDto;
import com.zufar.icedlatte.openapi.dto.ProductListWithPaginationInfoDto;
import com.zufar.icedlatte.product.converter.ProductInfoDtoConverter;
import com.zufar.icedlatte.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.zufar.icedlatte.common.util.Utils.createPageableObject;


@Slf4j
@Service
@RequiredArgsConstructor
public class PageableProductsProvider {

    private final ProductInfoRepository productInfoRepository;
    private final ProductInfoDtoConverter productInfoDtoConverter;
    private final ProductUpdater productUpdater;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductListWithPaginationInfoDto getProducts(final Pageable pageable,
                                                        final Integer priceFrom,
                                                        final Integer priceTo,
                                                        final Integer minimumAverageRating,
                                                        final Set<String> brandNames,
                                                        final Set<String> sellerNames) {
        Page<ProductInfoDto> productsWithPageInfo = productInfoRepository
                .findAllProducts(priceFrom, priceTo, minimumAverageRating, pageable)
                .map(productInfoDtoConverter::toDto)
                .map(productUpdater::update);

        return productInfoDtoConverter.toProductPaginationDto(productsWithPageInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ProductListWithPaginationInfoDto getProducts(final Integer page,
                                                        final Integer size,
                                                        final String sortAttribute,
                                                        final String sortDirection) {
        Pageable pageable = createPageableObject(page, size, sortAttribute, sortDirection);

        Page<ProductInfoDto> productsWithPageInfo = productInfoRepository
                .findAll(pageable)
                .map(productInfoDtoConverter::toDto)
                .map(productUpdater::update);

        return productInfoDtoConverter.toProductPaginationDto(productsWithPageInfo);
    }
}