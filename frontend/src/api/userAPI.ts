import axiosClient from "./axiosClient";

export interface UserDTO {
  id: string;
  userName: string;
  email: string;
  fullName?: string;
  phoneNumber?: string;
  status?: string;
  createdAt?: string;
  updatedAt?: string;
}

// Lấy danh sách user
export async function getUsers(): Promise<UserDTO[]> {
  const res = await axiosClient.get<UserDTO[]>("/users");
  return res.data;
}

// Tạo user mới (qua register)
export async function createUser(payload: {
  userName: string;
  email: string;
  password: string;
  fullName?: string;
  status?: string;
  phoneNumber?: string
}) {
  const res = await axiosClient.post("/auth/register", payload);
  return res.data;
}

// Update user
export async function updateUser(id: string, payload: {
  userName: string;
  email: string;
  password?: string;
  fullName?: string;
  status?: string;
  phoneNumber?: string
    }) {
  const res = await axiosClient.put(`/users/${id}`, payload);
  return res.data;
}

// Xoá user
export async function deleteUser(id: string) {
  await axiosClient.delete(`/users/${id}`);
}
