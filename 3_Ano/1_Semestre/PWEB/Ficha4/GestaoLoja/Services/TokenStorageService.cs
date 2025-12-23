namespace GestaoLoja.Services
{
    public class TokenStorageService
    {
        private string? _token;
        private DateTime? _expirationTime;

        public void setToken(string token, int expirationIn)
        {
            _token = token;
            _expirationTime = DateTime.UtcNow.AddSeconds(expirationIn);
        }

        public string? GetToken()
        {
            if (_token != null && _expirationTime.HasValue && DateTime.UtcNow < _expirationTime)
            {
                return _token;
            }
            ClearToken();
            return null;
        }

        public bool IsTokenValid()
        {
            return GetToken() != null;
        }

        public void ClearToken()
        {
            _token = null;
            _expirationTime = null;
        }
    }
}
