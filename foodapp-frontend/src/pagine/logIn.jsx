import React from 'react'

function LogIn() {
    return (
        <div id="login-section" className="flex flex-col items-center justify-center h-screen bg-yellow-100">
            <div >
                <h1 className="text-3xl font-bold  mb-6">Log In to Foodie's <span className="text-orange-400">Paradise</span></h1>
            </div>
            <div>
                <form className="bg-white p-6 rounded-2xl shadow-md w-80">
                    <div className="mb-4">
                        <label className="block text-orange-400 mb-2" htmlFor="email">Email</label>
                        <input type="email" id="email" className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-green-300" />
                    </div>
                    <div className="mb-6">
                        <label className="block text-orange-400  mb-2" htmlFor="password">Password</label>
                        <input type="password" id="password" className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-green-300" />
                    </div>
                    <button type="submit" className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600 transition">Log In</button>
                </form>
            </div>
        </div>
    )
}

export default LogIn
