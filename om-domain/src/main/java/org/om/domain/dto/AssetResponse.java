package org.om.domain.dto;

import org.om.domain.model.Asset;

public record AssetResponse(Long id, String assetName, int size, int usableSize) {

    public static AssetResponse fromAsset(Asset asset) {
        return new AssetResponse(asset.getId(), asset.getAssetName(), asset.getSize(), asset.getUsableSize());
    }

}
