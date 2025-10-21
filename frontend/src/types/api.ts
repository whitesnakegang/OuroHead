/**
 * 백엔드 APIResponse 타입 정의
 *
 * @template T - 성공 시 반환되는 데이터 타입
 * @template K - 에러 상세 정보 타입
 */

/**
 * API 에러 정보
 */
export interface APIError<K = unknown> {
  /** 에러 코드 (예: "SAVE_ERROR", "PREVIEW_ERROR" 등) */
  code: string;
  /** 에러 상세 설명 (선택적) */
  details?: K;
}

/**
 * 표준 API 응답 형식
 */
export interface APIResponse<T = unknown, K = unknown> {
  /** "success" 또는 "error" */
  status: 'success' | 'error';
  /** 성공 시 반환 데이터 */
  data?: T;
  /** 응답 메시지 */
  message: string;
  /** 에러 정보 (실패 시) */
  error?: APIError<K>;
}

/**
 * API 응답에서 데이터 추출 헬퍼 함수
 * @param response - APIResponse 형식의 응답
 * @returns 추출된 데이터
 * @throws {Error} status가 "error"인 경우 에러를 throw
 */
export function extractData<T, K = unknown>(response: APIResponse<T, K>): T {
  if (!response || typeof response !== 'object') {
    throw new Error('잘못된 API 응답 형식입니다.');
  }

  if (!response.status || !response.message) {
    throw new Error('APIResponse 형식이 아닙니다.');
  }

  if (response.status === 'success') {
    if (response.data === undefined) {
      throw new Error('성공 응답이지만 data가 없습니다.');
    }
    return response.data;
  } else {
    // 에러 응답 처리
    const errorCode = response.error?.code || 'UNKNOWN_ERROR';
    const errorMessage = response.message || 'API 요청 실패';
    throw new Error(`[${errorCode}] ${errorMessage}`);
  }
}

/**
 * API 응답에서 메시지 추출 헬퍼 함수
 * @param response - APIResponse 형식의 응답
 * @returns 메시지
 */
export function extractMessage<T = unknown, K = unknown>(
  response: APIResponse<T, K>
): string {
  if (!response || typeof response !== 'object') {
    return '잘못된 응답입니다.';
  }
  return response.message || '메시지가 없습니다.';
}

/**
 * API 응답에서 상태 추출 헬퍼 함수
 * @param response - APIResponse 형식의 응답
 * @returns 상태 ("success" 또는 "error")
 */
export function extractStatus<T = unknown, K = unknown>(
  response: APIResponse<T, K>
): 'success' | 'error' {
  if (!response || typeof response !== 'object') {
    return 'error';
  }
  return response.status || 'error';
}