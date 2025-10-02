// src/api/dishAPI.ts
import axiosClient from "./axiosClient";

export interface DishDTO {
  id: number;
  name: string;
  price: number;
  description: string;
  image: string;
  type: string;
  categoryId: number;
}

// Lấy danh sách dish
export async function getDishes(): Promise<DishDTO[]> {
  const res = await axiosClient.get("/dishes");
  return res.data.data;
}

// Tạo mới dish
export async function createDish(payload: Omit<DishDTO, "id">) {
  const res = await axiosClient.post("/dishes", payload);
  return res.data.data;
}

// Cập nhật dish
export async function updateDish(id: number, payload: Partial<DishDTO>) {
  const res = await axiosClient.put(`/dishes/${id}`, payload);
  return res.data.data;
}

// Xoá dish
export async function deleteDish(id: number) {
  const res = await axiosClient.delete(`/dishes/${id}`);
  return res.data.data;
}
