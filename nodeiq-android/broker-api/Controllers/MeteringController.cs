using BrokerApi.Data;
using BrokerApi.Data.Entities;
using Microsoft.AspNetCore.Mvc;

namespace BrokerApi.Controllers;

[ApiController]
[Route("metering")]
public class MeteringController : ControllerBase
{
    private readonly BrokerDb _db;

    public MeteringController(BrokerDb db) => _db = db;

    [HttpPost]
    public async Task<IActionResult> Record(MeteringRecord record)
    {
        _db.Metering.Add(record);
        await _db.SaveChangesAsync();
        return Ok(record);
    }
}
