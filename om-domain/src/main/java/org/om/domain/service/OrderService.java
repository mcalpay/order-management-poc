package org.om.domain.service;

import lombok.RequiredArgsConstructor;
import org.om.domain.dto.AssetResponse;
import org.om.domain.dto.NewOrder;
import org.om.domain.dto.NewOrderResponse;
import org.om.domain.dto.OrderResponse;
import org.om.domain.exception.OmAuthorizationException;
import org.om.domain.exception.OmException;
import org.om.domain.model.Asset;
import org.om.domain.model.Order;
import org.om.domain.model.OrderStatus;
import org.om.domain.repository.AssetRepository;
import org.om.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final String TRY_ASSET = "TRY";

    private final OrderRepository orderRepository;

    private final AssetRepository assetRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    public NewOrderResponse create(NewOrder newOrder) {
        if(newOrder.isSelling()){
            String assetName = newOrder.assetName();
            Optional<Asset> assetOptional =  assetRepository.findByCustomerIdAndAssetName(newOrder.customerId(), assetName);
            Asset asset = assetOptional.orElseThrow(OmException::newCustomerDoesNotHaveAssetException);
            if(asset.getUsableSize() < newOrder.size()) {
                throw OmException.newCustomerDoesNotHaveAssetException();
            }

            asset.setUsableSize(asset.getUsableSize() - newOrder.size());
            assetRepository.save(asset);

        } else {
            Optional<Asset> assetOptional =  assetRepository.findByCustomerIdAndAssetName(newOrder.customerId(), TRY_ASSET);
            Asset asset = assetOptional.orElseThrow(OmException::newCustomerDoesNotHaveAssetException);
            if(asset.getUsableSize() < newOrder.size() * newOrder.price()) {
                throw OmException.newCustomerDoesNotHaveAssetException();
            }

            asset.setUsableSize(asset.getUsableSize() - newOrder.size() * newOrder.price());
            assetRepository.save(asset);
        }

        Order save = orderRepository.save(newOrder.toEntity());
        return NewOrderResponse.fromOrder(save);
    }

    public List<OrderResponse> listOrders(Long customerId, String startDate, String endDate) {
        LocalDateTime start = LocalDate.parse(startDate, dateFormatter).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, dateFormatter).atStartOfDay();
        List<Order> orderList = orderRepository.findByCustomerIdAndCreateDateBetweenOrderByCreateDateDesc(customerId,
                start, end);
        return orderList.stream().map(OrderResponse::fromOrder).toList();
    }

    @Transactional
    public void deleteOrder(Long orderId, Long principalId, boolean isAdmin) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if(optional.isPresent()) {
            Order order = optional.get();
            if(!isAdmin && !principalId.equals(order.getCustomerId())) {
                throw new OmAuthorizationException();
            }
            if(OrderStatus.PENDING.equals(order.getStatus())) {
                order.setStatus(OrderStatus.CANCELED);
                if(order.isSelling()) {
                    Asset asset = assetRepository
                            .findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                            .orElseThrow(OmException::newCustomerDoesNotHaveAssetException);
                    asset.setUsableSize(asset.getUsableSize() + order.getSize());
                    assetRepository.save(asset);
                } else {
                    Asset asset = assetRepository
                            .findByCustomerIdAndAssetName(order.getCustomerId(), TRY_ASSET)
                            .orElseThrow(OmException::newCustomerDoesNotHaveAssetException);
                    asset.setUsableSize(asset.getUsableSize() + order.getSize() * order.getPrice());
                    assetRepository.save(asset);
                }
                orderRepository.save(order);
                return;
            }
        }

        throw OmException.newNoPendingOrderException();
    }

    public List<AssetResponse> listAssets(Long customerId) {
        return assetRepository.findByCustomerId(customerId).stream().map(AssetResponse::fromAsset).toList();
    }
}
