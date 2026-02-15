
export const CustomBtn = ({ onClick, type = "button", children }: any) => {
  return (
    <button
      onClick={onClick}
      type={type}
      className="text-white bg-blue-900 hover:bg-blue-800 px-6 py-3 rounded-lg text-lg font-medium transition-colors w-fit"
    >{children}</button>
  );
};
