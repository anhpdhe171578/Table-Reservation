import React from "react";
import { Link } from "react-router-dom";

const Sidebar: React.FC = () => {
  return (
    <aside className="w-64 bg-white/70 p-4 rounded shadow">
      <ul className="space-y-2">
        <li>
          <Link to="/users" className="block px-3 py-2 rounded hover:bg-sky-50">Users</Link>
        </li>
        <li>
          <Link to="/categories" className="block px-3 py-2 rounded hover:bg-sky-50">Categories</Link>
        </li>
        <li>
          <Link to="/dishes" className="block px-3 py-2 rounded hover:bg-sky-50">Dishes</Link>
        </li>
        <li>
          <Link to="/blogs" className="block px-3 py-2 rounded hover:bg-sky-50">Blogs</Link>
        </li>
      </ul>
    </aside>
  );
};

export default Sidebar;
