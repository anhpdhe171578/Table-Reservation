import axios from "axios";

const API_URL = "http://localhost:8080/api/auth";

export interface RegisterRequest {
  fullName: string;
  userName: string;
  email: string;
  phoneNumber: string;
  gender: string;
  password: string;
}

export interface LoginRequest {
  userName: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  fullName: string;
  email: string;
  phoneNumber: string;
  gender: string;
}

export async function register(data: RegisterRequest) {
  const res = await axios.post<AuthResponse>(`${API_URL}/register`, data);
  return res.data;
}

export async function login(data: LoginRequest) {
  const res = await axios.post<AuthResponse>(`${API_URL}/login`, data);
  return res.data;
}
