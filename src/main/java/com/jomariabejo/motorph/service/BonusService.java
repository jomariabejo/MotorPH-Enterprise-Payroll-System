package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Bonus;
import com.jomariabejo.motorph.repository.BonusRepository;

import java.util.List;

public class BonusService {

    private final BonusRepository bonusRepository;

    public BonusService(BonusRepository bonusRepository) {
        this.bonusRepository = bonusRepository;
    }

    public Bonus getBonusById(Integer id) {
        return bonusRepository.findById(id);
    }

    public List<Bonus> getAllBonuses() {
        return bonusRepository.findAll();
    }

    public void saveBonus(Bonus bonus) {
        bonusRepository.save(bonus);
    }

    public void updateBonus(Bonus bonus) {
        bonusRepository.update(bonus);
    }

    public void deleteBonus(Bonus bonus) {
        bonusRepository.delete(bonus);
    }
}
