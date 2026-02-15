import { Outlet } from "react-router-dom";

const Layout = () => {
  return (
    <div className="app-layout">
      <Outlet /> {/* Child routes render here */}
    </div>
  );
};

export default Layout;
