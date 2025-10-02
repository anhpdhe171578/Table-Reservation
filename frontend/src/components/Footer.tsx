import React from "react";

const Footer: React.FC = () => (
  <footer className="py-4 mt-8 text-center text-sm text-slate-600">
    © {new Date().getFullYear()} Demo App. Built with Spring Boot + React.
  </footer>
);

export default Footer;
