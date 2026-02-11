import React from 'react'
import { useState } from 'react'
import { useNavigate } from "react-router-dom";
function formLogIn() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) =>{
        //aggiornare lo stato del form grazie al nome del campo
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) =>{
        e.preventDefault();
        //mandare i dati al backend
        fetch('http://localhost:8080/api/users/login', {
            method: "POST",
            headers:{
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: formData.email,
                password: formData.password
            })
        })
        //gestire la risposta del backend e mostrare eventuali messaggi di successo o errore
        .then(res => res.json())
        .then(data => {
            //alert(data);

            if(data.message === "success"){
                //reindirizzare alla pagina del menu
                localStorage.setItem("userEmail", formData.email);
                localStorage.setItem("isLoggedIn", "true");
                navigate("/menu");
            }else{
                alert("Login failed: " + data);
            }
        })
        .catch(err => console.error(err));
        }
        return (
            <div className="h-screen bg-yellow-100 flex items-center justify-center">
                    <form onSubmit={handleSubmit} className=" mt-8 p-6 bg-white shadow-md w-full max-w-sm rounded-2xl"> 
                        <h2 className="text-2xl font-bold mb-6 text-center">Log In</h2>
                        <div className="mb-4">
                            <label htmlFor="email" className="block text-gray-700 font-bold mb-2">Email</label>
                            <input required name="email"  type="email" id="email" value={formData.email} onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Enter your email" />
                        </div>
                        <div className="mb-6">
                            <label htmlFor="password" className="block text-gray-700 font-bold mb-2">Password</label>
                            <input required name="password" type="password" id="password" value={formData.password} onChange={handleChange}  className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Enter your password" />
                        </div>
                        <button type="submit" className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600 cursor-pointer">Login</button>
                    </form>
            </div>
        )
    }



export default formLogIn
