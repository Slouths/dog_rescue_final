document.addEventListener('DOMContentLoaded', function() {
    const registrationForm = document.getElementById('registrationForm');
    const registerFormWrapper = document.getElementById('registerForm');
    const phoneFormWrapper = document.getElementById('phoneForm');
    const phoneNumberForm = document.getElementById('phoneNumberForm');
    const twoFactorForm = document.getElementById('twoFactorForm');
    
    // Handle initial registration
    registrationForm.addEventListener('submit', function(e) {
        e.preventDefault();
        registerFormWrapper.classList.add('hidden');
        phoneFormWrapper.classList.remove('hidden');
    });

    // Handle phone number submission
    phoneNumberForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const countryCode = document.getElementById('countryCode').value;
        const phoneNumber = document.getElementById('phoneNumber').value;
        
        try {
            // Send phone number to server and request OTP
            const response = await fetch('/api/2fa/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: document.querySelector('input[name="username"]').value,
                    phoneNumber: countryCode + phoneNumber
                })
            });
            
            if (response.ok) {
                // Show 2FA verification form
                phoneFormWrapper.classList.add('hidden');
                twoFactorForm.classList.remove('hidden');
                startCountdown();
            } else {
                alert('Error sending verification code. Please try again.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error sending verification code. Please try again.');
        }
    });

    // Countdown timer function
    function startCountdown() {
        let timeLeft = 5 * 60; // 5 minutes
        const countdownElement = document.getElementById('countdown');
        
        const timer = setInterval(() => {
            const minutes = Math.floor(timeLeft / 60);
            const seconds = timeLeft % 60;
            
            countdownElement.textContent = 
                `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
            
            if (timeLeft <= 0) {
                clearInterval(timer);
                countdownElement.textContent = "Code expired";
            }
            timeLeft--;
        }, 1000);
    }
});