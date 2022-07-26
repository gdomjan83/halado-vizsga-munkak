package training360.sportresults.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import training360.sportresults.dto.*;
import training360.sportresults.exception.AthleteNotFoundException;
import training360.sportresults.exception.ResultNotFoundException;
import training360.sportresults.model.Athlete;
import training360.sportresults.model.Result;
import training360.sportresults.model.SportType;
import training360.sportresults.repository.AthleteRepository;
import training360.sportresults.repository.ResultRepository;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AthleteResultService {
    private AthleteRepository athleteRepository;
    private ResultRepository resultRepository;
    private ModelMapper modelMapper;

    public AthleteDto saveAthlete(CreateAthleteCommand createAthleteCommand) {
        Athlete athlete = modelMapper.map(createAthleteCommand, Athlete.class);
        athleteRepository.save(athlete);
        return modelMapper.map(athlete, AthleteDto.class);
    }

    public AthleteDto saveResultToAthlete(long id, CreateResultCommand createResultCommand) {
        Athlete athlete = athleteRepository.findById(id).orElseThrow(() -> new AthleteNotFoundException(id));
        Result result = new Result(createResultCommand.getPlace(), createResultCommand.getResultDate(),
                createResultCommand.getSportType(), createResultCommand.getMeasure());
        athlete.addResult(result);
        resultRepository.save(result);
        return new AthleteDto(athlete.getId(), athlete.getName(), athlete.getSex(), createResultDtoList(athlete.getResults()));
    }

    public ResultDto updateResult(long id, UpdateMeasureCommand updateMeasureCommand) {
        Result result = resultRepository.findById(id).orElseThrow(() -> new ResultNotFoundException(id));
        result.setMeasure(updateMeasureCommand.getMeasure());
        ResultDto resultDto = modelMapper.map(result, ResultDto.class);
        resultDto.setAthleteName(result.getAthlete().getName());
        return resultDto;
    }

    public List<ResultRankingDto> getAllResults(Optional<SportType> sportType) {
        List<Result> found = resultRepository.getAllResults(sportType);
        if (sportType.isEmpty()) {
            return createResultRankingDto(found);
        } else {
            return orderResults(found, sportType);
        }
    }

    private List<ResultDto> createResultDtoList(List<Result> results) {
        return results.stream()
                .map(r -> new ResultDto(r.getId(), r.getPlace(), r.getResultDate(), r.getSportType(),
                        r.getMeasure(), r.getMeasureUnit(), r.getAthlete().getName()))
                .toList();
    }

    private List<ResultRankingDto> orderResults(List<Result> results, Optional<SportType> sportType) {
        switch (sportType.get()) {
            case SWIMMING:
            case RUNNING:
                return createResultRankingDto(results);
            default:
                List<ResultRankingDto> ranking = createResultRankingDto(results);
                return ranking.stream()
                        .sorted(Comparator.comparingDouble(ResultRankingDto::getMeasure).reversed())
                        .toList();
        }
    }

    private List<ResultRankingDto> createResultRankingDto(List<Result> results) {
        return results.stream()
                .map(r -> new ResultRankingDto(r.getId(), r.getAthlete().getName(), r.getPlace(), r.getResultDate(), r.getMeasure()))
                .toList();
    }
}
