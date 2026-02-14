import React from 'react'
import {useState} from 'react'
import { useNavigate } from 'react-router-dom';

function FormSignUp() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        city: '',
        address: ''
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
        fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers:{
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: formData.username,   
                email: formData.email,
                password: formData.password,
                city: formData.city,
                address: formData.address,
                role: "USER"  // puoi mettere default o lasciare che il backend lo gestisca  
            })
        })
        //gestire la risposta del backend e mostrare eventuali messaggi di successo o errore
        .then(res => res.json())
        .then(data => {
            //alert(data);

            if(data.message === "success"){
                //reindirizzare alla pagina del menu
                localStorage.setItem("userEmail", formData.email);
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
                <h2 className="text-2xl font-bold mb-6 text-center">Sign Up</h2>
                <div className="mb-4">
                    <label type="name" className="block text-gray-700 font-bold mb-2">Full Name</label>
                    <input  name="username" value={formData.username} required type="text" id="name" onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Enter your name" />
                </div>
                <div className="mb-4">
                    <label type="email" className="block text-gray-700 font-bold mb-2">Email</label>
                    <input name="email" value={formData.email} required type="email" id="email" onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Enter your email" />
                </div>
                <div className="mb-6">
                    <label type="password" className="block text-gray-700 font-bold mb-2">Password</label>
                    <input name="password" value={formData.password} required type="password" id="password" onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Enter your password" />
                </div>
                <div className="mb-6">
                    <label type="City" className="block text-gray-700 font-bold mb-2">City</label>
                    <input name="city" value={formData.city} required type="City" id="City" onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Confirm your password" />
                </div>
                <div className="mb-6">
                    <label type="address" className="block text-gray-700 font-bold mb-2">Address</label>
                    <input name="address" value={formData.address} required type="Address" id="address" onChange={handleChange} className="w-full px-3 py-2 border rounded focus:outline-none focus:ring focus:border-blue-300" placeholder="Confirm your password" />
                </div>
                <button type="submit" className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600 cursor-pointer">Sign Up</button>
            </form>
        </div>
    )
}

export default FormSignUp
