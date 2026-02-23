import React, { useEffect, useState } from "react";

function Success() {
  
  return (
    <div className="p-10 text-center">
      <h1 className="text-3xl font-bold mb-4 text-green-600">
        ✅ Pagamento completato!
      </h1>

      <button
        onClick={() => (window.location.href = "/")}
        className="mt-6 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition"
      >
        Torna al menu
      </button>
    </div>
  );
}

export default Success;