

                  Player tries to buzz
                           │
                           ▼
              [ Pre-Check (No Lock) ]
       Is buzzer OPEN and player NOT locked out?
             │                        │
            YES                       NO ──► Rejected immediately
             │
             ▼
      [ tryLock() Acquisition ] ──► Fails if another thread holds the lock
             │
             ▼
      [ Post-Check (Under Lock) ]
       Is buzzer STILL OPEN? 
       (Prevents Thread A and B passing the pre-check simultaneously)
             │                        │
            YES                       NO ──► Unlock & Reject
             │
             ▼
       Buzzer status set to LOCKED
       activeBuzzerHolder = Player
       
  