import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import Navbar from './components/Navbar.jsx'
import Home from './pagine/Home.jsx'
import Menu from './pagine/Menu.jsx'
import LogIn from './pagine/LogIn.jsx'
import SignUp from './pagine/SignUp.jsx'

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/menu" element={<Menu />} />
        <Route path="/LogIn" element={<LogIn />} />
        <Route path="/SignUp" element={<SignUp />} /> 
      </Routes>
    </Router>
  )
}

export default App
