/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef ANDROID_HWUI_PATH_CACHE_H
#define ANDROID_HWUI_PATH_CACHE_H

#include <SkBitmap.h>
#include <SkPaint.h>
#include <SkPath.h>

#include "Texture.h"
#include "utils/Compare.h"
#include "utils/GenerationCache.h"

namespace android {
namespace uirenderer {

/**
 * Describe a path in the path cache.
 */
struct PathCacheEntry {
    PathCacheEntry() {
        path = NULL;
        join = SkPaint::kDefault_Join;
        cap = SkPaint::kDefault_Cap;
        style = SkPaint::kFill_Style;
        miter = 4.0f;
        strokeWidth = 1.0f;
    }

    PathCacheEntry(const PathCacheEntry& entry):
        path(entry.path), join(entry.join), cap(entry.cap),
        style(entry.style), miter(entry.miter),
        strokeWidth(entry.strokeWidth) {
    }

    PathCacheEntry(SkPath* path, SkPaint* paint) {
        this->path = path;
        join = paint->getStrokeJoin();
        cap = paint->getStrokeCap();
        miter = paint->getStrokeMiter();
        strokeWidth = paint->getStrokeWidth();
        style = paint->getStyle();
    }

    SkPath* path;
    SkPaint::Join join;
    SkPaint::Cap cap;
    SkPaint::Style style;
    float miter;
    float strokeWidth;

    bool operator<(const PathCacheEntry& rhs) const {
        LTE_INT(path) {
            LTE_INT(join) {
                LTE_INT(cap) {
                    LTE_INT(style) {
                        LTE_FLOAT(miter) {
                            LTE_FLOAT(strokeWidth) return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}; // struct PathCacheEntry

/**
 * Alpha texture used to represent a path.
 */
struct PathTexture: public Texture {
    PathTexture(): Texture() {
    }

    /**
     * Left coordinate of the path bounds.
     */
    float left;
    /**
     * Top coordinate of the path bounds.
     */
    float top;
    /**
     * Offset to draw the path at the correct origin.
     */
    float offset;
}; // struct PathTexture

/**
 * A simple LRU path cache. The cache has a maximum size expressed in bytes.
 * Any texture added to the cache causing the cache to grow beyond the maximum
 * allowed size will also cause the oldest texture to be kicked out.
 */
class PathCache: public OnEntryRemoved<PathCacheEntry, PathTexture*> {
public:
    PathCache();
    PathCache(uint32_t maxByteSize);
    ~PathCache();

    /**
     * Used as a callback when an entry is removed from the cache.
     * Do not invoke directly.
     */
    void operator()(PathCacheEntry& path, PathTexture*& texture);

    /**
     * Returns the texture associated with the specified path. If the texture
     * cannot be found in the cache, a new texture is generated.
     */
    PathTexture* get(SkPath* path, SkPaint* paint);
    /**
     * Clears the cache. This causes all textures to be deleted.
     */
    void clear();
    /**
     * Removes an entry.
     */
    void remove(SkPath* path);

    /**
     * Sets the maximum size of the cache in bytes.
     */
    void setMaxSize(uint32_t maxSize);
    /**
     * Returns the maximum size of the cache in bytes.
     */
    uint32_t getMaxSize();
    /**
     * Returns the current size of the cache in bytes.
     */
    uint32_t getSize();

private:
    /**
     * Generates the texture from a bitmap into the specified texture structure.
     */
    void generateTexture(SkBitmap& bitmap, Texture* texture);

    PathTexture* addTexture(const PathCacheEntry& entry, const SkPath *path, const SkPaint* paint);

    void init();

    GenerationCache<PathCacheEntry, PathTexture*> mCache;

    uint32_t mSize;
    uint32_t mMaxSize;
    GLuint mMaxTextureSize;

    /**
     * Used to access mCache and mSize. All methods are accessed from a single
     * thread except for remove().
     */
    mutable Mutex mLock;
}; // class PathCache

}; // namespace uirenderer
}; // namespace android

#endif // ANDROID_HWUI_PATH_CACHE_H
