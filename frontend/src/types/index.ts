// API 관련 타입 정의
export interface Field {
  name: string;
  type: string;
  description?: string;
  required?: boolean;
  defaultValue?: string;
  fakerType?: string;
}

export interface Response {
  statusCode: number;
  response: any;
}

export interface Endpoint {
  path: string;
  method: string;
  description: string;
  responses: Response[];
  requiresAuth?: boolean;
  authType?: string;
  authHeader?: string;
  request?: {
    type: string;
    contentType?: string;
    fields: Field[];
  };
}

export interface ApiDefinition {
  endpoints: Endpoint[];
}

export interface PreviewData {
  error?: string;
  [key: string]: any;
}
