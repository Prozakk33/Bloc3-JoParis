const form = document.getElementById('signupForm');

form.addEventListener('submit', (event) => {

    event.preventDefault(); 
    // Create a new instance of the user class
    var firstName = document.getElementById("firstName").value;
    var lastName = document.getElementById("lastName").value;
    var email = document.getElementById("email").value;
    var password = document.getElementById("password").value;

    let newUser = 
    {
      "firstName" : firstName,
      "lastName" : lastName,
      "email" : email,
      "password" : password
    };

    signUpApi(newUser);
}

async function signUpApi(newUser) {

    const response = await fetch('/user/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newUser)
    });

    if (response.ok) {
        const data = await response.json();
        console.log('User created successfully:', data);
    } else {
        console.error('Error creating user:', response.statusText);
    }
}

