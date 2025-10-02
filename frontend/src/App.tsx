import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./pages/Home";
import UsersPage from "./pages/UsersPage";
import CategoriesPage from "./pages/CategoriesPage";
import DishesPage from "./pages/DishesPage";
import BlogsPage from "./pages/BlogsPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import TablesPage from "./pages/TablesPage";
import AreasPage from "./pages/AreasPage";
import RestaurantsPage from "./pages/RestaurantsPage";
import OrdersPage from "./pages/OrdersPage";

function App() {
  return (
    <Router>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/users" element={<UsersPage />} />
            <Route path="/categories" element={<CategoriesPage />} />
            <Route path="/dishes" element={<DishesPage />} />
            <Route path="/blogs" element={<BlogsPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/tables" element={<TablesPage />} />
            <Route path="/areas" element={<AreasPage />} />
            <Route path="/restaurants" element={<RestaurantsPage />} />
            <Route path="/orders" element={<OrdersPage />} />

          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;
