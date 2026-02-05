import React from 'react'
import NavBar from '../components/navbar'
function Home() {
    return (
        <div>
            <div id="welcome-section" className="flex flex-col items-center justify-center h-screen bg-yellow-100">
                <h1 className="text-5xl md:text-6xl font-bold text-green-800 mb-6">Welcome to Foodie's Paradise</h1>
                <p className="text-xl md:text-2xl text-green-700 mb-8 text-center px-4 md:px-0 max-w-2xl"></p>
            </div>
        </div>
    )
}

export default Home
