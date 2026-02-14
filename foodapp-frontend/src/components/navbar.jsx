import React from "react";
import { Link, useNavigate } from "react-router-dom";

function NavBar() {
    const navigate = useNavigate();
    const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";

    const token = localStorage.getItem("token");
    let role = null;
    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            role = payload.role;
        } catch (err) {
            console.error("Token JWT non valido:", err);
        }
    }

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.setItem("isLoggedIn", "false");
        navigate("/LogIn");
    };

    return (
        <nav className="flex items-center justify-between p-4 bg-white shadow-md">
            <div id="Logo">
                <h1 className="text-xl text-green-500 font-bold">
                    <Link to="/">Foodie's <span className="text-orange-400">Paradise</span></Link> 
                </h1>
            </div>
            <div id="Links" className="space-x-6 font-bold max-md:hidden">
                <Link to="/" className="hover:text-green-500">Home</Link>
                <Link to="/menu" className="hover:text-green-500">Menu</Link>
            </div>
            <div id="Actions" className="space-x-4 max-md:hidden">
                {isLoggedIn ? (
                    <>
                        {role === "ADMIN" && (
                            <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 cursor-pointer">
                                <Link to="admin/admin_home">Admin</Link>
                            </button>
                        )}
                        <button onClick={handleLogout} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 cursor-pointer">
                            Log Out
                        </button>
                    </>
                ) : (
                    <>
                        <button className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 cursor-pointer">
                            <Link to="/LogIn">Log In</Link>
                        </button>
                        <button className="px-4 py-2 bg-orange-400 text-white rounded hover:bg-orange-500 cursor-pointer">
                            <Link to="/SignUp">Sign Up</Link>
                        </button>
                    </>
                )}
            </div>
        </nav>
    );
}

export default NavBar;
