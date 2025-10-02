import axios from "axios";

export interface RestaurantDTO {
  id: number;
  address: string;
  createdAt: string;
  updatedAt: string;
}

const API_URL = "http://localhost:8080/api/restaurants"; // chỉnh lại nếu BE khác

export async function getRestaurants(): Promise<RestaurantDTO[]> {
  const res = await axios.get(API_URL);
  return res.data.data;
}

export async function createRestaurant(data: Omit<RestaurantDTO, "id" | "createdAt" | "updatedAt">) {
  const res = await axios.post(API_URL, data);
  return res.data.data;
}

export async function updateRestaurant(id: number, data: Partial<RestaurantDTO>) {
  const res = await axios.put(`${API_URL}/${id}`, data);
  return res.data.data;
}

export async function deleteRestaurant(id: number) {
  await axios.delete(`${API_URL}/${id}`);
}
