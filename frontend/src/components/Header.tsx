import React from "react";
import { Link } from "react-router-dom";

const Header: React.FC = () => {
  return (
    <header className="bg-white/60 backdrop-blur-md shadow sticky top-0 z-20">
      <div className="container mx-auto px-4 py-3 flex items-center justify-between">
        <Link to="/" className="text-2xl font-extrabold text-sky-700">
          Restaurant
        </Link>

      </div>
    </header>
  );
};

export default Header;
