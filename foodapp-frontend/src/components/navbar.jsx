import React from "react";
import {Link} from "react-router-dom";

function NavBar() {
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
                <button className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 ">Login</button>
                <button className="px-4 py-2 bg-orange-400 text-white rounded hover:bg-orange-500">Sign Up</button>
            </div>
        </nav>
    )
}
export default NavBar;