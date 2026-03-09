// src/lib/types.ts

export interface ApiError {
    message: string;
    timestamp: string;
}

export interface UrlResponseCompleteDto {
    id: number;
    originalUrl: string;
    shortUrl: string;
    clicks: number;
    expirationType: 'PERMANENT' | 'SEVEN_DAYS' | 'ONE_DAY';
    creationAt: string;
    expiredAt: string | null;
}

// Alias para compatibilidade com código existente
export type UrlDto = UrlResponseCompleteDto;

export interface LoginResponseDto {
    token: string;
}

export interface PageResponse<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
    };
    totalElements: number;
    totalPages: number;
    last: boolean;
    first: boolean;
}