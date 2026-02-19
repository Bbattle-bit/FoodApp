import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import Navbar from './components/Navbar.jsx'
import Home from './pagine/Home.jsx'
import Menu from './pagine/Menu.jsx'
import LogIn from './pagine/LogIn.jsx'
import SignUp from './pagine/SignUp.jsx'
import Home_admin from './pagine_admin/home_admin.jsx'
import Gestione_ordini from './pagine_admin/gestione_ordini.jsx'
import Carrello from './pagine/Carrello.jsx'
import {CartProvider} from './pagine/CartContext.jsx'

function App() {
  return (
    <CartProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/menu" element={<Menu />} />
          <Route path="/LogIn" element={<LogIn />} />
          <Route path="/SignUp" element={<SignUp />} /> 
          <Route path="admin/admin_home" element={<Home_admin />} />
          <Route path="admin/gestione_ordini" element={<Gestione_ordini />} />
          <Route path="/carrello" element={<Carrello />} />
        </Routes>
      </Router>
    </CartProvider>
  )
}

export default App
