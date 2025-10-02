// src/api/categoryAPI.ts
import axiosClient from "./axiosClient";

export interface CategoryDTO {
  id: number;
  name: string;
  createdAt: string;
  updatedAt: string;
}

// Lấy danh sách category
export async function getCategories(): Promise<CategoryDTO[]> {
  const res = await axiosClient.get("/categories");
  return res.data.data; // backend trả { data: [...] }
}

// Tạo mới category
export async function createCategory(payload: Omit<CategoryDTO, "id" | "createdAt" | "updatedAt">) {
  const res = await axiosClient.post("/categories", payload);
  return res.data.data;
}

// Cập nhật category
export async function updateCategory(id: number, payload: Partial<CategoryDTO>) {
  const res = await axiosClient.put(`/categories/${id}`, payload);
  return res.data.data;
}

// Xoá category
export async function deleteCategory(id: number) {
  const res = await axiosClient.delete(`/categories/${id}`);
  return res.data.data;
}
