import axiosClient from "./axiosClient";


export interface OrderDTO {
  orderID: number;
  userId: string;
  tableID: number;
  createdAt: string;
  reservationTime: string;
  status: string;
  orderItems: OrderItemDTO[];
  totalAmount: number;
}

export interface OrderItemDTO {
  id: number;
  dish: {
    id: number;
    name: string;
    price: number;
  };
  quantity: number;
  price: number;
  amount: number;
}

// Lấy tất cả orders
export async function getOrders(): Promise<OrderDTO[]> {
  const res = await axiosClient.get("/orders");
  return res.data.data || res.data;
}

// Lấy chi tiết order theo ID
export async function getOrderById(id: number): Promise<OrderDTO> {
  const res = await axiosClient.get(`/orders/${id}`);
  return res.data.data || res.data;
}

// Thêm order mới (tương ứng customer/receptionist)
export async function createOrder(payload: Partial<OrderDTO>, type: "customer" | "receptionist") {
  const url = type === "customer" ? "/orders/customer/create" : "/orders/receptionist/create";
  const res = await axiosClient.post(url, payload);
  return res.data.data || res.data;
}

// Cập nhật order item
export async function updateOrder(id: number, payload: Partial<OrderDTO>) {
  const res = await axiosClient.put(`/orders/${id}/update-item`, payload);
  return res.data.data || res.data;
}

// Xóa order item
export async function deleteOrder(id: number) {
  const res = await axiosClient.delete(`/orders/${id}/remove-item`);
  return res.data.data || res.data;
}
