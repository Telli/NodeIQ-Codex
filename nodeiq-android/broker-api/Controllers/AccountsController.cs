using BrokerApi.Data;
using BrokerApi.Data.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace BrokerApi.Controllers;

[ApiController]
[Route("accounts")]
public class AccountsController : ControllerBase
{
    private readonly BrokerDb _db;

    public AccountsController(BrokerDb db) => _db = db;

    [HttpPost]
    public async Task<IActionResult> Create(Account account)
    {
        _db.Accounts.Add(account);
        await _db.SaveChangesAsync();
        return CreatedAtAction(nameof(Get), new { peerId = account.PeerId }, account);
    }

    [HttpGet("{peerId}")]
    public async Task<IActionResult> Get(string peerId)
    {
        var account = await _db.Accounts.FirstOrDefaultAsync(a => a.PeerId == peerId);
        return account is null ? NotFound() : Ok(account);
    }
}
