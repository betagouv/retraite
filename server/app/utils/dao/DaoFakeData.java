package utils.dao;

import java.util.List;

import models.FakeData;

public class DaoFakeData {

	public List<FakeData> findAll() {
		return FakeData.findAll();
	}

}
