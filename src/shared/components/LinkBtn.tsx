import { Link } from "react-router-dom";

const LinkBtn = ({ to, title }: any) => {
  return (
    <Link
      to={to}
      className="text-white bg-blue-900 hover:bg-blue-800 px-6 py-3 rounded-lg text-lg font-medium transition-colors w-fit"
    >
      {title}
    </Link>
  );
};

export default LinkBtn;
